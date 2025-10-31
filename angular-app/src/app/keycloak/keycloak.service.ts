import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({ providedIn: 'root' })
export class KeycloakService {
  private keycloak?: Keycloak;
  private initPromise?: Promise<boolean>;

  init(): Promise<boolean> {
    if (this.initPromise) {
      return this.initPromise;
    }

    try {
      this.keycloak = new Keycloak({
        // Use empty string for relative URLs so Angular dev proxy handles CORS
        // Keycloak.js will construct /realms/keynote-app/... which the proxy forwards
        url: '',
        realm: 'keynote-app',
        clientId: 'gateway-client',
      });

      console.log('Keycloak instance created', this.keycloak);

      // Check if we're in the middle of a Keycloak callback
      const isKeycloakCallback =
        window.location.hash.includes('code=') ||
        window.location.search.includes('code=') ||
        window.location.hash.includes('state=');

      console.log('Keycloak callback detected:', isKeycloakCallback);
      console.log('Current URL:', window.location.href);

      this.initPromise = this.keycloak
        .init({
          onLoad: isKeycloakCallback ? 'check-sso' : 'login-required',
          checkLoginIframe: false,
          pkceMethod: 'S256',
          // Use query mode to avoid hash issues with router
          responseMode: 'query',
          // Be explicit about redirect URI
          redirectUri: window.location.origin + window.location.pathname,
        })
        .then((authenticated) => {
          console.log('Keycloak init successful, authenticated:', authenticated);
          console.log('Keycloak instance state:', {
            authenticated: this.keycloak?.authenticated,
            token: this.keycloak?.token ? 'present' : 'missing',
          });

          // If we're coming back from Keycloak callback, don't redirect again
          if (!isKeycloakCallback && !this.keycloak?.authenticated) {
            console.log('Not authenticated, redirecting to login...');
            this.keycloak?.login();
          }
          return true;
        })
        .catch((err) => {
          console.error('Keycloak initialization error caught');
          console.error('Error type:', typeof err);
          console.error('Error value:', err);
          console.error('Error stringified:', JSON.stringify(err));
          console.error('Error toString:', err?.toString());
          console.error('Full error object:', err);

          // Check if Keycloak server is reachable
          fetch('http://localhost:8080/realms/keynote-app/.well-known/openid-configuration')
            .then((res) => {
              console.log('Keycloak server reachable:', res.ok, res.status);
            })
            .catch((fetchErr) => {
              console.error('Keycloak server NOT reachable:', fetchErr);
            });

          // Do not block app bootstrap; render app and allow manual login
          return true;
        });
    } catch (error) {
      console.error('Error creating Keycloak instance:', error);
      this.initPromise = Promise.resolve(true);
    }

    return this.initPromise;
  }

  getToken(): Promise<string> {
    if (!this.keycloak || !this.keycloak.authenticated) {
      return Promise.reject('Keycloak not authenticated');
    }
    return this.ensureTokenFreshness().then(() => this.keycloak!.token as string);
  }

  private ensureTokenFreshness(): Promise<boolean> {
    if (!this.keycloak) return Promise.reject('Keycloak not initialized');
    return this.keycloak
      .updateToken(30)
      .catch(() => {
        this.login();
        return Promise.reject('Token refresh failed');
      })
      .then(() => true);
  }

  isAuthenticated(): boolean {
    return this.keycloak?.authenticated ?? false;
  }

  login(): void {
    this.keycloak?.login();
  }

  logout(): void {
    this.keycloak?.logout({ redirectUri: window.location.origin });
  }
}
