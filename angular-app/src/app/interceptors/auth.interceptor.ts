import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Interceptor not needed - AuthService handles token in ConferencesService directly
  // This is kept for potential future use
  return next(req);
};
