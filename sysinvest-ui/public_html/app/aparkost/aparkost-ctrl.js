
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
            var towerId = 0;
            if($scope.temp.tower && $scope.temp.tower.id){
                towerId = $scope.temp.tower.id;
            }
            $scope.data.investment = [];
            $http.get("/api/investment/ret/byflooroftower?towerid=" + towerId + "&floor=" + $scope.temp.level)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.investments = response.data;
                        $scope.temp.investment = response.data[0];
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

        $scope.save = function () {
            var payload = {};
            payload.aparkost = {};
            payload.aparkost.tower = $scope.temp.tower;
            payload.aparkost.index = parseInt($scope.temp.index);
            payload.aparkost.name = $scope.temp.prefix + $scope.temp.index;
            payload.aparkost.floor = $scope.temp.level;
            payload.marketRate = $scope.temp.rate;
            payload.flag = 1;
            payload.marketRateUpdate = new Date();

            $log.debug(payload);
            $http.post('/api/investment/save', payload)
                    .success(function (response) {
                        alert("Data Aparkost telah disimpan!");
                        $log.debug(response);
                        $scope.selectFloorOfTower();
                    }).error(function (err) {
                $log.debug(err);
            });
        };

    }


})();
