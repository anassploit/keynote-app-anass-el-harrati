import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, switchMap, map } from 'rxjs';
import { AuthService } from './auth.service';

export interface Conference {
  title: string;
  type: string;
  date: string;
  time: number;
  nbSubscribed: number;
  keynotes: unknown;
  score: number;
  _links: {
    self: { href: string };
    conference: { href: string };
    reviews: { href: string };
  };
}

interface HalConferences {
  _embedded: { conferences: Conference[] };
  _links: unknown;
  page: { size: number; totalElements: number; totalPages: number; number: number };
}

@Injectable({ providedIn: 'root' })
export class ConferencesService {
  private readonly url = '/conference-service/api/conferences';

  constructor(private http: HttpClient, private auth: AuthService) {}

  getConferences(): Observable<Conference[]> {
    return this.auth.getAccessToken().pipe(
      switchMap((token) => {
        const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
        return this.http.get<HalConferences>(this.url, { headers });
      }),
      map((hal) => hal._embedded?.conferences ?? [])
    );
  }
}
