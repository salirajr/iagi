
(function () {
    var app = angular.module('my-spa-client', ['ui.router', 'oc.lazyLoad', 'ui.bootstrap', 'ngStorage']);

    app.config(config);
    app.run(run);

    angular.getApplicationModule = function () {
        return angular.module(app.name);
    };


    function run($rootScope, $http, $location, $localStorage, $log, $state, AuthenticationService) {

        $rootScope.$state = $state;

        /*$rootScope.$on('$stateChangeStart', function (e, toState, toParams, fromState, fromParams) {

            var stateRoles = toState.data.roles;
            var isPublicState = !stateRoles || stateRoles.indexOf('public') >= 0;

            if (!isPublicState) {
                if (AuthenticationService.isLoggedIn()) {
                    var hasPermission = false;
                    for (var i in stateRoles) {
                        var stateRole = stateRoles[i];
                        if (AuthenticationService.getCurrentRoles().indexOf(stateRole) >= 0) {
                            hasPermission = true;
                            break;
                        }
                    }
                    if (hasPermission === false) {
                        var params = {msg: 'No access'};
                        $log.debug(params.msg);
                        $log.debug('redirect state to login');
                        e.preventDefault();
                        $state.go('login', params);
                    } else {

                    }
                } else {
                    var params = {msg: 'Not logged in'};
                    $log.debug(params.msg);
                    $log.debug('redirect state to login');
                    e.preventDefault();
                    $state.go('login', params);
                }
            }
        });*/
    }

    function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {

        $urlRouterProvider.otherwise('/index/main');

        $ocLazyLoadProvider.config({
            debug: true
        });

        $stateProvider
                .state('login', {
                    url: '/login?msg',
                    data: {pageTitle: 'Login'},
                    templateUrl: 'app/login/login-two-columns.html',
                    controller: 'LoginCtrl',
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/login/login-ctrl.js');
                        }
                    }
                })
                .state('index', {
                    abstract: true,
                    url: "/index",
                    templateUrl: "app/common/main-layout-view.html"
                })
                .state('index.main', {
                    url: "/main",
                    data: {pageTitle: 'Main', roles: ['admin']},
                    templateUrl: "app/main/main.html",
                    controller: "MainCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/main/main-ctrl.js');
                        }
                    }
                })
                .state('index.minor', {
                    url: "/minor",
                    data: {pageTitle: 'Minor'},
                    templateUrl: "app/minor/minor.html",
                    controller: "MinorCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/minor/minor-ctrl.js');
                        }
                    }
                })
                .state('index.investor', {
                    url: "/investor",
                    data: {pageTitle: 'Investor'},
                    templateUrl: "app/investor/investor.html",
                    controller: "InvestorCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/investor/investor-ctrl.js');
                        }
                    }
                })
                ;
    }
})();

