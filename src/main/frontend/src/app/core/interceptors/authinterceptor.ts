import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';

@Injectable()
export class Authinterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if ( request.url.includes('login') ) {
      request = request.clone({headers: this.getHeaders(request.headers) });
    }
    return next.handle(request);
  }
  private getHeaders(headers: HttpHeaders): HttpHeaders {
      let newHeader = headers.set('X-Requested-With', 'XMLHttpRequest');
      newHeader = newHeader.set('content-type', 'application/json');
      return newHeader;
  }
}
