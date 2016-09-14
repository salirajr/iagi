
(function () {

    angular.getApplicationModule()
            .controller('AparkostCtrl', AparkostCtrl);

    /*
     * MainCtrl - controller
     */
    function AparkostCtrl($scope, $log, $http) {
        $log.debug('AparkostCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

        $scope.data = {};

        $scope.dInput = {};


        $scope.getSite = function () {
            $http.get("/api/site/ret?key=")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.site = response.data;
                        $scope.dInput.selectedSiteId = response.data[0].id;
                        $scope.data.selectedSite = response.data[0];
                        $log.debug("getSite responsed sucess with " + $scope.dInput.selectedSiteId);
                        $scope.selectSite();
                    });

        };
        $scope.getSite();
        $scope.selectSite = function () {
            $log.debug("selectSite called with " + $scope.dInput.selectedSiteId);
            $http.get("/api/tower/ret/bysiteid?value=" + $scope.dInput.selectedSiteId)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.tower = response.data;
                        $scope.dInput.selectedTowerId = response.data[0].id;
                        $scope.data.selectedTower = response.data[0];
                        $log.debug("getSite responsed sucess with " + $scope.dInput.selectedTowerId);
                        $scope.selectFloorOfTower();
                    });
        };

        $scope.dInput.prefix = 0;
        $scope.selectFloorOfTower = function () {

            $scope.data.investment = [];
            $http.get("/api/investment/ret/byflooroftower?towerid=" + $scope.dInput.selectedTowerId + "&floor=" + $scope.dInput.selectedFloor)
                    .then(function (response) {
                        $log.debug(response);
                        if (response.data.length <= 0) {
                            return;
                        }
                        $scope.data.investment = response.data;
                        $scope.dInput.selectedInvestmentId = response.data[0].id;
                        $scope.data.selectedInvestment = response.data[0];
                        $scope.selectInvestment();
                    });

            switch ($scope.dInput.selectedFloor) {
                case "G" :
                    $scope.dInput.prefix = 100;
                    break;
                case "1" :
                    $scope.dInput.prefix = 200;
                    break;
                case "2" :
                    $scope.dInput.prefix = 300;
                    break;
                case "3" :
                    $scope.dInput.prefix = 400;
                    break;
            }
        };

        $scope.toInt = function (n) {
            return parseInt(n);

        }
        $scope.selectInvestment = function () {
            $scope.data.selectedInvestment = {};

            for (var i = 0; i < $scope.data.investment.length; i++) {
                if ($scope.data.investment[i].id === $scope.dInput.selectedInvestmentId) {
                    angular.copy($scope.data.investment[i], $scope.data.selectedInvestment);
                    break;
                }
            }
        };

        $scope.save = function () {
            var payload = {};
            payload.aparkost = {};
            payload.aparkost.tower = $scope.data.selectedTower;
            payload.aparkost.index = $scope.dInput.index;
            payload.aparkost.name = $scope.dInput.prefix + $scope.toInt($scope.dInput.index) ;
            payload.aparkost.floor = $scope.dInput.selectedFloor;
            payload.marketRate = $scope.dInput.rate;
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
