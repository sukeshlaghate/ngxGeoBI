import { urlBase64Decode} from '@nebular/auth/helpers';

export abstract class NgxAuthToken{
  protected payload: any = null;
  abstract getValue(): string;
  abstract isValid(): boolean;

  //the stregy name used to acquire this token
  abstract getOwnerStrategyName(): string;
  abstract getCreatedAt(): Date;
  abstract toString(): string;

  getName(): string {
    return (this.constructor as NgxAuthTokenClass).NAME;
  }
  getPayload(): any {
    return this.payload;
  }
}

export class NgxAuthTokenNotFoundError extends Error{
  constructor (message: string){
    super(message);
    Object.setPrototypeOf(this, new.target.prototype);
  }
}

export class NgxAuthIllegalTokenError extends Error{
  constructor( message: string) {
    super(message);
    Object.setPrototypeOf(this, new.target.prototype);
  }
}

export class NgxAuthEmptyTokenError extends NgxAuthIllegalTokenError {
  constructor(message: string) {
    super(message);
    Object.setPrototypeOf(this, new.target.prototype);
  }
}

export class NgxAuthIllegalJWTTokenError extends NgxAuthIllegalTokenError {
  constructor(message: string) {
    super(message);
    Object.setPrototypeOf(this, new.target.prototype);
  }
}

export interface NgxAuthRefreshableToken {
  getrefreshToken(): string;
  setRefreshToken(refreshTOken: string);
}

export interface NgxAuthTokenClass<T = NgxAuthToken> {
  NAME: string;
  new ( raw: any, strategyName: string, expDate?: Date): T;
}

export function NgxAuthCreateToken< T extends NgxAuthToken>( tokenClass: NgxAuthTokenClass<T>,
                                                             token: any,
                                                             ownerStrategyName: string,
                                                             createdAt?: Date) {
  return new tokenClass(token, ownerStrategyName, createdAt);
}

export function decodeJwtPayload(payload: string): any {
  if (payload.length === 0 ) {
    throw new NgxAuthEmptyTokenError('Cannot extract from an empty payload.');
  }
  const parts = payload.split('.');
  if ( parts.length !== 3) {
    throw new NgxAuthIllegalJWTTokenError(`The payload ${payload} is not valid JWT payload and must consist of three parts.`);
  }
  let decoded;
  try{
    decoded = urlBase64Decode( parts[1]);
  } catch(e) {
    throw new NgxAuthIllegalJWTTokenError(`The payload ${payload} is not valid JWT payload and cannot be parsed.`);
  }
  if (!decoded) {
    throw new NgxAuthIllegalJWTTokenError(
      `The payload ${payload} is not valid JWT payload and cannot be decoded.`);
  }
  return JSON.parse(decoded);
}


