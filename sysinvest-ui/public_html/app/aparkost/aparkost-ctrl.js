
(function () {

    angular.getApplicationModule()
            .controller('AparkostCtrl', AparkostCtrl);

    /*
     * MainCtrl - controller
     */
    function AparkostCtrl($scope, $log, $http) {

        $scope.getTowers = function () {
            $log.debug($scope.temp.site);
            $http.get("/api/tower/ret/bysiteid?value=" + $scope.temp.site.id)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.towers = response.data;
                        $scope.temp.tower = response.data[0];
                        $log.debug($scope.temp.tower);
                        $scope.getInvestments();
                    });
        };

        $scope.getInvestments = function () {
            $scope.temp.investment = {};
            var towerId = 0;
            if ($scope.temp.tower && $scope.temp.tower.id) {
                towerId = $scope.temp.tower.id;
            }
            $scope.data.investment = [];
            $http.get("/api/investment/ret/byflooroftower?towerid=" + towerId + "&floor=" + $scope.temp.level)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.investments = response.data;
                        $scope.temp.investment = response.data[0];
                        $scope.setInvestment();
                    });

            switch ($scope.temp.level) {
                case "G" :
                    $scope.temp.prefix = "0";
                    break;
                default :
                    $scope.temp.prefix = $scope.temp.level;
                    break;
            }
        };

        function initiate() {
            $scope.data = {};
            $scope.temp = {};
            $scope.temp.tower = {};
            $scope.temp.level = 'G';

            $scope.lookup = {};

            $http.get("/api/site/ret?key=")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.sites = response.data;
                        $scope.temp.site = response.data[0];

                        $scope.getTowers();
                    });


        }
        initiate();

        $scope.setInvestment = function () {
            if ($scope.temp.investment && $scope.temp.investment.aparkost) {

                switch ($scope.temp.investment.aparkost.floor) {
                    case "G" :
                        $scope.temp.prefix = "0";
                        break;
                    default :
                        $scope.temp.prefix = $scope.temp.investment.aparkost.floor;
                        break;
                }
                $scope.temp.index = $scope.temp.investment.aparkost.index < 10 ? "0" + $scope.temp.investment.aparkost.index : $scope.temp.investment.aparkost.index + "";
                $scope.temp.rate = $scope.temp.investment.marketRate;

                $scope.temp.level = $scope.temp.investment.aparkost.floor;
                $scope.temp.investment.marketRateUpdate = new Date();
            }
        };

        $scope.isAvailability = function () {
            for (var i = 0; i < $scope.lookup.investments.length; i++) {
                if ($scope.lookup.investments[i].name === ($scope.temp.prefix + $scope.temp.index)) {
                    angular.copy($scope.lookup.investments[i], $scope.temp.investment);
                    $log.debug($scope.temp.investment);
                    break;
                }
            }
            $scope.temp.investment.id = undefined;
            $log.debug($scope.temp.investment);
        };


        $scope.save = function () {

            var payload = {};
            if ($scope.temp.investment && $scope.temp.investment.aparkost) {
                angular.copy($scope.temp.investment, payload);
            } else {
                payload.aparkost = {};
                payload.aparkost.floor = $scope.temp.level;
                payload.flag = 1;
            }
            payload.aparkost.tower = $scope.temp.tower;
            payload.aparkost.index = parseInt($scope.temp.index);
            payload.aparkost.name = $scope.temp.prefix + $scope.temp.index;

            payload.marketRate = $scope.temp.rate;
            payload.marketRateUpdate = new Date();
            
            $log.debug(payload);

            $http.post('/api/investment/save', payload)
                    .success(function () {
                        alert("Data Aparkost telah disimpan!");
                        $scope.getInvestments();

                    }).error(function (err) {
                $log.debug(err);
            });
        };

    }


})();
