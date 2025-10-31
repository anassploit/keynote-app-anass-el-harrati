import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { KeynotesService, Keynote } from '../services/keynotes.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-keynotes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './keynotes.component.html',
  styleUrl: './keynotes.component.css',
})
export class KeynotesComponent implements OnInit {
  keynotes = signal<Keynote[]>([]);
  isLoading = signal<boolean>(false);
  error = signal<string | null>(null);

  constructor(
    private keynotesService: KeynotesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.load();
  }

  private load(): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.keynotesService.getKeynotes().subscribe({
      next: (items) => {
        this.keynotes.set(items);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Keynotes load error', err);
        console.error('Error details:', {
          status: err.status,
          statusText: err.statusText,
          message: err.message,
          url: err.url,
          error: err.error,
        });

        let errorMessage = 'Failed to load keynotes. Please try again.';
        if (err.status === 401) {
          errorMessage = 'Authentication failed. Please login again.';
          this.authService.logout();
          this.router.navigate(['/login']);
        } else if (err.status === 404) {
          errorMessage = 'Keynotes endpoint not found. Please check the API configuration.';
        } else if (err.status === 0) {
          errorMessage = 'Cannot connect to the server. Please check if the API is running.';
        } else if (err.status) {
          errorMessage = `Error ${err.status}: ${err.statusText || 'Failed to load keynotes'}`;
        }

        this.error.set(errorMessage);
        this.isLoading.set(false);
      },
    });
  }
}
