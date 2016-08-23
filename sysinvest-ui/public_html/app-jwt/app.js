(function () {
    'use strict';

    angular
            .module('app', ['ui.router', 'ngMessages', 'ngStorage' /*, 'ngMockE2E' */])
            .config(config)
            .run(run);

    function config($stateProvider, $urlRouterProvider) {
        // default route
        $urlRouterProvider.otherwise("/");
        // app routes
        $stateProvider
                .state('home', {
                    url: '/',
                    templateUrl: 'app-parts/home/home.view.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                })
                .state('login', {
                    url: '/login',
                    templateUrl: 'app-parts/login/login.view.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                });
    }

    function run($rootScope, $http, $location, $localStorage, $log) {
        // keep user logged in after page refresh
        if ($localStorage.currentUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
        }
        // redirect to login page if not logged in and trying to access a restricted page
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            $log.debug("$locationChangeStart current=" + JSON.stringify(current)+", next=" + JSON.stringify(next));
            var publicPages = ['/login'];
            var restrictedPage = publicPages.indexOf($location.path()) === -1;
            if (restrictedPage && !$localStorage.currentUser) {
                $log.debug("Not logged in, redirect to login page");
                $location.path('/login');
            }
        });
    }
})();