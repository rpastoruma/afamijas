// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const BASE_URL = '/api/';
const AUTH_URL = '/api/auth/';
const ADMIN_URL = '/api/private/';
const MEDIA_URL = 'https://afamijas.org/media'

export const environment = {
    production: false,
    url: {
      base: BASE_URL,
      auth : AUTH_URL,
      login: AUTH_URL + 'login',
      config: BASE_URL + 'configuration',
      notifications : BASE_URL + 'notifications'
    }
  };
  
  /*
   * For easier debugging in development mode, you can import the following file
   * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
   *
   * This import should be commented out in production mode because it will have a negative impact
   * on performance if an error is thrown.
   */
  // import 'zone.js/dist/zone-error';  // Included with Angular CLI.
  