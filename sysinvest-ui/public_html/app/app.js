
(function () {
    var app = angular.module('my-spa-client', ['ui.router', 'oc.lazyLoad', 'ui.bootstrap', 'ngStorage', 'ui.mask']);

    app.config(config);
    app.run(run);

    angular.getApplicationModule = function () {
        return angular.module(app.name);
    };


    function run($rootScope, $http, $location, $localStorage, $log, $state, AuthenticationService) {

        // used by pageTitle directive
        $rootScope.appTitle = "IAGI";
        //$rootScope.$state = $state;
        $rootScope.data = {};

        $rootScope.currentUser = AuthenticationService.isLoggedIn();

        $rootScope.$on('$stateChangeStart', function (e, toState, toParams, fromState, fromParams) {

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
        });

        $rootScope.doLogout = function () {

            if (AuthenticationService.isLoggedIn()) {
                AuthenticationService.doLogout();
                $state.go('login');
            }
        };

        $rootScope.todayDate = function () {
            return new Date().toISOString().slice(0, 10);
        };

        $rootScope.handleError = function (err, fnCallback) {
            $log.debug(err);
            try {
                var data = JSON.parse(err.data.message);
                if (data.code && data.code === 500 && data.message === "Invalid token.") {
                    $state.go('login');
                }
            } catch (e) {
                $log.debug("handleError: e.stackTrace()");
                $log.debug(e);
            }
            fnCallback;
        };

    }

    function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {

        $urlRouterProvider.otherwise('/index/acquisition');

        $ocLazyLoadProvider.config({
            debug: true
        });

        $stateProvider
                .state('login', {
                    url: '/login?msg',
                    data: {pageTitle: 'Login'},
                    templateUrl: 'app/login/sysinvest.html',
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
                .state('index.aparkost', {
                    url: "/aparkost",
                    data: {pageTitle: 'Aparkost', roles: ['admin']},
                    templateUrl: "app/aparkost/aparkost.html",
                    controller: "AparkostCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/aparkost/aparkost-ctrl.js');
                        }
                    }
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
                .state('index.investor', {
                    url: "/investor",
                    data: {pageTitle: 'Investor', roles: ['admin']},
                    templateUrl: "app/investor/investor.html",
                    controller: "InvestorCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/investor/investor-ctrl.js');
                        }
                    }
                })
                .state('index.investorlist', {
                    url: "/investorlist",
                    data: {pageTitle: 'Register Investor', roles: ['admin']},
                    templateUrl: "app/investor/investorlist.html",
                    controller: "InvestorListCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/investor/investorlist-ctrl.js');
                        }
                    }
                })
                .state('index.covenant', {
                    url: "/covenant",
                    data: {pageTitle: 'Covenant', roles: ['admin']},
                    templateUrl: "app/covenant/covenant.html",
                    controller: "CovenantCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/covenant/covenant-ctrl.js');
                        }
                    }
                })
                .state('index.booking', {
                    url: "/booking",
                    data: {pageTitle: 'Booking', roles: ['admin']},
                    templateUrl: "app/covenant/booking.html",
                    controller: "BookingCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/covenant/booking-ctrl.js');
                        }
                    }
                })
                .state('index.bookinglist', {
                    url: "/bookinglist",
                    data: {pageTitle: 'Booking', roles: ['admin']},
                    templateUrl: "app/covenant/bookinglist.html",
                    controller: "BookingListCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/covenant/bookinglist-ctrl.js');
                        }
                    }
                })
                .state('index.acquisition', {
                    url: "/acquisition",
                    data: {pageTitle: 'Acquisition', roles: ['admin']},
                    templateUrl: "app/acquisition/acquisition.html",
                    controller: "AcquisitionCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/acquisition/acquisition-ctrl.js');
                        }
                    }
                })
                .state('index.staff', {
                    url: "/staff",
                    data: {pageTitle: 'Staff', roles: ['admin']},
                    templateUrl: "app/staff/staff.html",
                    controller: "StaffCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/staff/staff-ctrl.js');
                        }
                    }
                })
                .state('index.stafflist', {
                    url: "/stafflist",
                    data: {pageTitle: 'StaffList', roles: ['admin']},
                    templateUrl: "app/staff/stafflist.html",
                    controller: "StaffListCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/staff/stafflist-ctrl.js');
                        }
                    }
                })
                .state('index.lookup', {
                    url: "/lookup",
                    data: {pageTitle: 'Lookup Reference', roles: ['admin']},
                    templateUrl: "app/lookup/lookup.html",
                    controller: "LookupCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/lookup/lookup-ctrl.js');
                        }
                    }
                })
                .state('index.lookupakad', {
                    url: "/lookupakad",
                    data: {pageTitle: 'Lookup Akad Reference', roles: ['admin']},
                    templateUrl: "app/lookup/lookupakad.html",
                    controller: "LookupAkadCtrl",
                    resolve: {
                        load: function ($ocLazyLoad) {
                            return $ocLazyLoad.load('app/lookup/lookupakad-ctrl.js');
                        }
                    }
                })
                ;
    }
})();

