import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, map, of, tap, throwError } from 'rxjs';

interface TokenResponse {
  access_token: string;
  expires_in: number;
  refresh_token?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenUrl = '/realms/keynote-app/protocol/openid-connect/token';
  private token: string | null = null;
  private tokenExpiry: number | null = null;

  constructor(private http: HttpClient) {
    // Load token from localStorage on service initialization
    this.loadTokenFromStorage();
  }

  private loadTokenFromStorage(): void {
    const storedToken = localStorage.getItem('access_token');
    const storedExpiry = localStorage.getItem('token_expiry');

    if (storedToken && storedExpiry) {
      const expiryTime = parseInt(storedExpiry, 10);
      if (expiryTime > Date.now()) {
        this.token = storedToken;
        this.tokenExpiry = expiryTime;
      } else {
        this.clearToken();
      }
    }
  }

  private saveTokenToStorage(token: string, expiresIn: number): void {
    this.token = token;
    // Set expiry time slightly before actual expiry for safety
    this.tokenExpiry = Date.now() + (expiresIn - 60) * 1000;
    localStorage.setItem('access_token', token);
    localStorage.setItem('token_expiry', this.tokenExpiry.toString());
  }

  private clearToken(): void {
    this.token = null;
    this.tokenExpiry = null;
    localStorage.removeItem('access_token');
    localStorage.removeItem('token_expiry');
  }

  isAuthenticated(): boolean {
    return this.token !== null && this.tokenExpiry !== null && this.tokenExpiry > Date.now();
  }

  login(username: string, password: string): Observable<void> {
    const body = new HttpParams()
      .set('client_id', 'gateway-client')
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password);

    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });

    return this.http.post<TokenResponse>(this.tokenUrl, body.toString(), { headers }).pipe(
      tap((response) => {
        this.saveTokenToStorage(response.access_token, response.expires_in);
      }),
      map(() => void 0)
    );
  }

  logout(): void {
    this.clearToken();
  }

  getAccessToken(): Observable<string> {
    if (this.isAuthenticated() && this.token) {
      return of(this.token);
    }

    return throwError(() => new Error('Not authenticated. Please login.'));
  }
}
