import { Routes } from '@angular/router';
import { ConferencesComponent } from './conferences/conferences.component';
import { KeynotesComponent } from './keynotes/keynotes.component';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'conferences', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'conferences', component: ConferencesComponent, canActivate: [authGuard] },
  { path: 'keynotes', component: KeynotesComponent, canActivate: [authGuard] },
];
