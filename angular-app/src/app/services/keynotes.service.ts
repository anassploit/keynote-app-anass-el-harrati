import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, switchMap, map } from 'rxjs';
import { AuthService } from './auth.service';

export interface Keynote {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  function: string;
  _links: {
    self: { href: string };
    keynote: { href: string };
  };
}

interface HalKeynotes {
  _embedded: { keynotes: Keynote[] };
  _links: unknown;
  page: { size: number; totalElements: number; totalPages: number; number: number };
}

@Injectable({ providedIn: 'root' })
export class KeynotesService {
  private readonly url = '/keynote-service/api/keynotes';

  constructor(private http: HttpClient, private auth: AuthService) {}

  getKeynotes(): Observable<Keynote[]> {
    return this.auth.getAccessToken().pipe(
      switchMap((token) => {
        const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
        return this.http.get<HalKeynotes>(this.url, { headers });
      }),
      map((hal) => hal._embedded?.keynotes ?? [])
    );
  }
}
