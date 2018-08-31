import {Inject, Injectable} from '@angular/core';
import { Observable, of as observableOf} from 'rxjs/Rx';
import { switchMap, map } from 'rxjs/operators';

import { NgxAuthStrategy } from '../strategies/auth-strategy';
import { NGX_AUTH_STRATEGIES } from '../auth.options';
import { NgxAuthResult } from './auth-result';
import { NgxTokenService } from './token/token.service';
import { NgxAuthToken } from './token/token';

/**
 * Common aauthentication service Should be used as an interlayer between UI components and Auth Strategy
 */
@Injectable()
export class NgxAuthService {
  constructor(protected tokenService: NgxTokenService,
              @Inject(NGX_AUTH_STRATEGIES) protected strategies) {
  }

  /**
   * Retrives current authenticated token stored
   * @returns { Observable<NgsAuthToken> }
   */
  getToken(): Observable<NgxAuthToken>{
    return this.tokenService.get();
  }

  /**
   * Return true if auth toekn is presented in the token storage
   * @return { Observable<boolean> }
   */
  isAuthenticaged(): Observable<boolean> {
    return this.getToken().pipe(map((token: NgxAuthToken) => token.isValid()));
  }

  /**
   * Returns tokens stream
   * @returns { Observalve<NgxAuthToken> }
   */
  onTokenChange(): Observable<NgxAuthToken> {
    return this.tokenService.tokenChange();
  }

  /**
   * Returns authentication status stream
   * @returns { observalbe<boolean> }
   */
  onAuthenticationChange(): Observable<boolean>{
    return this.onTokenChange().pipe(map((token: NgxAuthToken)=> token.isValid()));
  }

  /**
   * AUthenticates with the selected strategy
   * Stores received toek in the token storage
   *
   * Example:
   * authenticate('username', {username: 'user.name', password: 'Password'})
   *
   * @param strategyName
   * @param data
   * @returns { Observable<NgxAUthResult> }
   */
  authenticate(strategyName: string, data?: any): Observable <NgxAuthResult>{
    return this.getStrategy(strategyName).authenticate(data).pipe(
        switchMap((result: NgxAuthResult) => {
          return this.processResultToken(result);
        }));
  }

  /**
   * Registers with the selected strategy
   * Stores received token in the token storage
   *
   * Example:
   * register('email', {email:'example@email.com, name:'John Doe', password:'Passwrd'})
   *
   * @param strategyName : the name of the strategy to use when calling register method
   * @param data: the data required for register api call of the backend
   * @returns { Observable<NgxAuthResult> }
   */
    register( strategyName: string, data?: any ): Observalbe<NgxAuthResult> {
      return this.getStrategy(strategyName).register(data)
        .pipe(
          switchMap((result: NgxAuthResult) => {
            return this.processResultToken(result);
          })
        );
   }

  /**
   * Sign outs with the selected strategy.
   * Removes toke from the token storage
   */
  logout( strategyName: string): Observable<NgxAUthResult>{
    return this.getStrategy(strategyName).logout()
      .pipe(
        switchMap((result:NgxAUthResult) => {
          if (result.isSuccess()) {
            this.tokenService.clear().pipe(map(() => result));
          }
          return observableOf(result);
        })
      )
  }

  /**
   * Sends forgot passwod request to the selected strategy
   *
   */
  requestPassword(strategyName: string, data?: any): Observable<NgxAuthResult> {
    return this.getStrategy(strategyName).requestPassword(data);
  }

  /**
   * Tries to reset password with the selected strategy
   *
   * Example:
   * resetPassword('email', {newPassword: 'test'})
   *
   * @param strategyName
   * @param data
   * @returns {Observable<NbAuthResult>}
   */
    resetPassword(strategyName: string, data?: any): Observable<NgxAuthResult>{
      return this.getStrategy(strategyName).resetPassword(data);
  }

  /**
   * Sends a refresh token request
   * Stores received token in the token storage
   *
   * Example:
   * refreshToken('email', {token: token})
   *
   * @param {string} strategyName
   * @param data
   * @returns {Observable<NbAuthResult>}
   */
  refreshToken(strategyName: string, data?: any): Observable<NgxAuthResult>{
    return this.getStrategy(strategyName).refreshToken(data)
      .pipe(
        switchMap((result: NgxAuthResult) => {
          return this.processResultToken(result)
        })
      );
  }

  /**
   * Get registered strategy by name
   *
   * Example: getStrategy('usernamePassword')
   *
   * @param {string} provider
   * returns {NgxAuthStrategy}
   */
  protected getStrategy(strategyName: string): NgxAuthStrategy {
    const found = this.strategies.find((strategY: NgxAuthStrategy) => strategy.getName() === strategyName);

    if (!found) {
      throw new TypeError(`There is no Auth Strategy registred under '${strategyName}' name`);
    }
    return found;
  }
  // little hack to store refresh tokens
  private processResultToken(result: NgxAuthResult) {
    if ( result.isSuccess() && result.getToken()) {
      if (result.hasRefreshToken() && result.getRefreshToken()) {
        this.tokenService.setRefreshToken(result.getRefreshToken());
      }
      return this.tokenService.set(result.getToken()).pipe(
        map((token: NgxAuthToken) => {
          return result;
        }) );
    }
    return observableOf(result);
  }
}
