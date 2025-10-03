// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const BASE_URL = '/api/';
const AUTH_URL = '/api/auth/';
const ADMIN_URL = '/api/private/';
const MEDIA_URL = 'https://afamijas.org/media'

export const environment = {
    production: true,
    url: {
      base: BASE_URL,
      auth : AUTH_URL,
      login1: AUTH_URL + 'login/step1',
      login2: AUTH_URL + 'login/step2',
      config: BASE_URL + 'configuration',
      notifications : ADMIN_URL + 'notifications',
      relatives : ADMIN_URL + 'relatives',
      workers : ADMIN_URL + 'workers',
      users : ADMIN_URL + 'users',
      members : ADMIN_URL + 'members',
      volunteers : ADMIN_URL + 'volunteers',
      patients : ADMIN_URL + 'patients',
      invoices : ADMIN_URL + 'invoices',
      media : ADMIN_URL + 'media'

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
  