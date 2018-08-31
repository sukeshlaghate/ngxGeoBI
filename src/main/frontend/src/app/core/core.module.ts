import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NbAuthModule, NbDummyAuthStrategy, NbPasswordAuthStrategy, NbAuthJWTToken,
  NbAuthJWTInterceptor
} from '@nebular/auth';
import { NbSecurityModule, NbRoleProvider } from '@nebular/security';
import { of as observableOf } from 'rxjs';

import { throwIfAlreadyLoaded } from './module-import-guard';
import { DataModule } from './data/data.module';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {Authinterceptor} from './interceptors/authinterceptor';
// import { AnalyticsService } from './utils/analytics.service';

const socialLinks = [
  {
    url: 'https://github.com/akveo/nebular',
    target: '_blank',
    icon: 'socicon-github',
  },
  {
    url: 'https://www.facebook.com/akveo/',
    target: '_blank',
    icon: 'socicon-facebook',
  },
  {
    url: 'https://twitter.com/akveo_inc',
    target: '_blank',
    icon: 'socicon-twitter',
  },
];


const formSetting: any = {
  redirectDelay: 0,
  showMessages: {
    success: true,
    error: true,
  },
};

const loginFormSetting: any = {
  redirectDelay: 0,
  showMessages: {
    success: true,
    error: true,
  },
  socialLinks: socialLinks,
};
export class NbSimpleRoleProvider extends NbRoleProvider {
  getRole() {
    // here you could provide any role based on any auth flow
    return observableOf('guest');
  }
}

export const NB_CORE_PROVIDERS = [
  ...DataModule.forRoot().providers,
  // ...NbAuthModule.forRoot({
  //
  //   strategies: [
  //     NbDummyAuthStrategy.setup({
  //       name: 'email',
  //       delay: 3000,
  //     }),
  //   ],
  //   forms: {
  //     login: {
  //       socialLinks: socialLinks,
  //     },
  //     register: {
  //       socialLinks: socialLinks,
  //     },
  //   },
  // }).providers,
    ... NbAuthModule.forRoot(
      {
        strategies: [
          NbPasswordAuthStrategy.setup({
            name: 'email',
            baseEndpoint: '/api/auth/',
            token: {
              class: NbAuthJWTToken,
              key: 'accessToken'
            },
            login: {
              endpoint: 'login',
              method: 'post',
            },
            register: {
              endpoint: 'register',
            },
            logout: {
              endpoint: 'logout',
            },
            requestPass: {
              endpoint: 'request-pass',
            },
            resetPass: {
              endpoint: 'reset-pass',
            },
          }),
        ],
        forms: {
          login: loginFormSetting,
          register: formSetting,
          requestPassword: formSetting,
          resetPassword: formSetting,
          logout: {
            redirectDelay: 0,
          },
        },
      },
    ).providers,

  NbSecurityModule.forRoot({
    accessControl: {
      guest: {
        view: '*',
      },
      user: {
        parent: 'guest',
        create: '*',
        edit: '*',
        remove: '*',
      },
    },
  }).providers,

  {
    provide: NbRoleProvider, useClass: NbSimpleRoleProvider,
  },
  // AnalyticsService,
];

@NgModule({
  imports: [
    CommonModule,
  ],
  exports: [
    NbAuthModule,
  ],
  declarations: [],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }

  static forRoot(): ModuleWithProviders {
    return <ModuleWithProviders>{
      ngModule: CoreModule,
      providers: [
        ...NB_CORE_PROVIDERS,
        { provide: HTTP_INTERCEPTORS, useClass: NbAuthJWTInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: Authinterceptor, multi: true },
      ],
    };
  }
}
