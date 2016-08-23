(function () {
    'use strict';
    angular.getApplicationModule()
            .factory('AuthenticationService', AuthenticationService);
    function AuthenticationService($http, $localStorage, $log) {

        // keep user logged in after page refresh
        if (isLoggedIn()) {
            setupHttpHeader();
        }

        return {
            doLogin: Login,
            doLogout: Logout,
            isLoggedIn: isLoggedIn,
            getCurrentUser: getCurrentUser,
            getCurrentRoles: getCurrentRoles
        };

        function getCurrentRoles() {
            return getCurrentUser() ? getCurrentUser().roles : undefined;
        }

        function getCurrentUser() {
            return $localStorage.currentUser;
        }

        function setupHttpHeader() {
            // add jwt token to auth header for all requests made by the $http service
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
        }

        function isLoggedIn() {
            return $localStorage.currentUser;
        }

        function Login(username, password, callback) {
            $http.post('/login-api', {username: username, password: password})
                    .success(function (response) {
                        $log.debug("LoginResponse=" + JSON.stringify(response));
                        // login successful if there's a token in the response
                        if (response.token) {
                            // store username and token in local storage to keep user logged in between page refreshes
                            $localStorage.currentUser = {
                                username: username, 
                                token: response.token,
                                roles:response.roles
                            };

                            setupHttpHeader();
                            // execute callback with true to indicate successful login
                            callback(true);
                        } else {
                            // execute callback with false to indicate failed login
                            callback(false);
                        }
                    });
        }

        function Logout() {
            // remove user from local storage and clear http auth header
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = undefined;
        }
    }
})();