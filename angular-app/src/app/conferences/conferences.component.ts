import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ConferencesService, Conference } from '../services/conferences.service';

@Component({
  selector: 'app-conferences',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './conferences.component.html',
  styleUrl: './conferences.component.css',
})
export class ConferencesComponent implements OnInit {
  conferences = signal<Conference[]>([]);
  isLoading = signal<boolean>(false);
  error = signal<string | null>(null);
  Math = Math; // Expose Math for template use

  constructor(private conferencesService: ConferencesService) {}

  ngOnInit(): void {
    this.load();
  }

  private load(): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.conferencesService.getConferences().subscribe({
      next: (items) => {
        this.conferences.set(items);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load conferences. Please try again.');
        console.error('Conferences load error', err);
        this.isLoading.set(false);
      },
    });
  }
}
