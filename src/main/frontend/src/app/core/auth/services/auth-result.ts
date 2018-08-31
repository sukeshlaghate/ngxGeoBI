import { NgxAuthToken } from './token/token';

export class NgxAuthResult {
  protected token: NgxAuthToken;
  protected refreshToken: NgxAuthToken;
  protected errors: string[] = [];
  protected messages: string[] = [];

  constructor( protected success: boolean,
               protected refreshTokenSuccess: boolean = false,
               protected response?: any,
               protected redirect?: any,  errors?: any,
               messages?: any,
               token: NgxAuthToken = null,
               refreshToken: NgxAuthToken = null) {

    if (errors instanceof Array){
      this.errors = errors;
    } else {
      this.errors = this.errors.concat([errors]);
    }

     if (messages instanceof Array) {
       this.messages = messages;
     } else {
      this.messages = this.messages.concat([messages]);
     }

     this.token = token;
     this.refreshToken = refreshToken;
  }

  /**
   * Returns the raw response recieved
   * @returns {any}
   */
  getResponse(): any {
    return this.response;
  }

  getToken(): NgxAuthToken {
    return this.token;
  }

  getRefreshToken(): NgxAuthToken {
    return this.refreshToken;
  }
  getRedirect(): string {
    return this.redirect;
  }

  getErrors(): string[] {
    return this.errors.filter( val => !!val);
  }

  getMessages():string[] {
    return this.messages.filter( val => !!val);
  }

  hasRefreshToken(): boolean {
    return this.refreshTokenSuccess;
  }

  isSuccess(): boolean {
    return this.success;
  }

  isFailure(): boolean {
    return !this.success;
  }
}
