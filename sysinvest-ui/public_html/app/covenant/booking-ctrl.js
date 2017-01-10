
(function () {

    angular.getApplicationModule()
            .controller('BookingCtrl', BookingCtrl);

    /*
     * MainCtrl - controller
     */
    function BookingCtrl($rootScope, $scope, $log, $http, $window) {


        $scope.getTowers = function () {
            $http.get("/api/tower/ret/bysiteid?value=" + $scope.temp.site.id)
                    .then(function (response) {
                        $scope.lookup.towers = response.data;
                        $scope.temp.tower = response.data[0];
                        $scope.getInvestments();
                    });
        };

        $scope.getInvestments = function () {
            $http.get("/api/investment/ret/onsale/bytowerid?value=" + $scope.temp.tower.id)
                    .then(function (response) {
                        $scope.lookup.investments = response.data;
                        $scope.temp.investment = response.data[0];
                    });
        };

        function initiate() {

            $scope.lookup = {};
            $scope.data = {};
            $scope.data.booking = {};
            $scope.data.booking.broker = {};
            $scope.data.booking.marketRate = 0;
            $scope.data.booking.rate = 0;
            $scope.data.booking.investments = [];

            $scope.temp = {};
            $scope.temp.site = {};
            $scope.temp.tower = {};
            $scope.temp.booked = {};
            $scope.temp.isSiteFormDisabled = true;


            $http.get("/api/staff/ret/byusername?value=" + $rootScope.currentUser.username)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.booking.broker = response.data;
                    });

            $http.get("/api/site/ret?key=")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.sites = response.data;
                        $scope.temp.site = response.data[0];
                        $scope.getTowers();
                    });
        }
        initiate();
        $scope.addInvestment = function () {
            var investment = {};
            angular.copy($scope.temp.investment, investment);
            var isExist = false;
            var i = 0;
            for (i = 0; i < $scope.data.booking.investments.length; i++) {
                if ($scope.data.booking.investments[i].id === investment.id) {
                    isExist = true;
                    break;
                }
            }
            if (isExist) {
                var isConfirm = confirm("Aparkost ini telah masuk dalam daftar investasi, apakah anda ingin menggantinya?");
                if (isConfirm) {
                    $scope.data.booking.marketRate += parseInt(investment.marketRate) - parseInt($scope.data.booking.investments[i].marketRate);
                    $scope.data.booking.rate += parseInt(investment.soldRate) - parseInt($scope.data.booking.investments[i].soldRate);
                    $scope.data.booking.investments[i] = investment;
                }
            } else {
                $scope.data.booking.investments.push(investment);
                $scope.data.booking.marketRate += parseInt(investment.marketRate);
                $scope.data.booking.rate += parseInt(investment.soldRate);
            }
        };

        $scope.removeInvestment = function () {
            var investment = {};
            angular.copy($scope.temp.investment, investment);
            var isConfirm = confirm("Apakah anda akan menbatalkan booking aparkost " + investment.aparkost.name + " lt. " + investment.aparkost.floor);
            if (isConfirm) {
                for (i = 0; i < $scope.data.booking.investments.length; i++) {
                    if ($scope.data.booking.investments[i].id === investment.id) {
                        $scope.data.booking.investments.splice(i, 1);
                        $scope.data.booking.marketRate -= parseInt(investment.marketRate);
                        $scope.data.booking.rate -= parseInt(investment.soldRate);
                        break;
                    }
                }
            }

        };

        $scope.setInvestment = function () {
            angular.copy($scope.temp.sInvestment, $scope.temp.investment);
        };

        $scope.doBook = function () {
            var isConfirm = confirm("Apakah anda ingin menyimpan data rekord berikut?");
            if (!isConfirm)
                return;

            var payload = {};
            angular.copy($scope.data.booking, payload);
            $http.post('/api/booking/addnew', payload)
                    .success(function (response) {
                        $scope.data.booking = response;
                        $log.debug(response);
                        alert("Data Booking telah disimpan!");
                    }).error(function (err) {
                $log.debug(err);
            });
        };
    }


})();
