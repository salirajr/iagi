
(function () {

    angular.getApplicationModule()
            .controller('TowerCtrl', TowerCtrl);

    /*
     * MainCtrl - controller
     */
    function TowerCtrl($scope, $log, $http) {

        $scope.getTowers = function () {
            angular.copy($scope.temp.site, $scope.data.site);
            $http.get("/api/tower/ret/bysiteid?value=" + $scope.data.site.id)
                    .then(function (response) {
                        $scope.lookup.towers = response.data;
                        angular.copy(response.data[0], $scope.data.tower);
                        $scope.temp.tower = response.data[0];
                    });
        };

        $scope.getSite = function () {
            $http.get("/api/site/ret?key")
                    .then(function (response) {
                        $scope.lookup.sites = response.data;

                        $scope.temp.site = response.data[0];
                        angular.copy(response.data[0], $scope.data.site);
                        $scope.getTowers();
                    });
        };

        function initiate() {

            $scope.lookup = {};
            $scope.data = {};
            $scope.data.site = {};
            $scope.data.tower = {};

            $scope.temp = {};
            $scope.temp.site = {};
            $scope.temp.tower = {};
            $scope.temp.isSiteFormDisabled = true;

            $scope.getSite();

            $http.get("/api/lookup/ret/findByGroupName?value=PROVINCE")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.provinces = response.data;
                    });
        }
        initiate();

        $scope.setTower = function () {
            angular.copy($scope.temp.tower, $scope.data.tower);
        };

        $scope.setSiteForm = function () {
            $scope.temp.isSiteFormDisabled = !$scope.temp.isSiteFormDisabled;
        };

        $scope.addSite = function () {
            alert("Site akan ditambahkan.");
            var temp = {};
            angular.copy(temp, $scope.data.site);
        };

        $scope.saveSite = function () {
            var isConfirm = confirm("Anda akan menyimpan Site berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            var payload = {};
            angular.copy($scope.data.site, payload);
            $http.post('/api/site/save', payload)
                    .success(function (response) {
                        $log.debug(response);
                        $scope.data.site = response;
                        alert("Site id dengan [" + $scope.data.site.id + "] telah disimpan!");
                        $scope.getSite();
                        $scope.getTower();
                    }).error(function (err) {
                alert("Site id dengan [" + $scope.data.site.id + "] gagal tersimpan!");
                $log.debug(err);
            });
        };

        $scope.addTower = function () {
            alert("Tower akan ditambahkan.");
            var temp = {};
            temp.site = $scope.data.site;
            angular.copy(temp, $scope.data.tower);
        };

        $scope.saveTower = function () {
            var isConfirm = confirm("Anda akan menyimpan Tower berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            var payload = {};
            $scope.data.tower.site = $scope.data.site;
            angular.copy($scope.data.tower, payload);
            $log.debug(payload);
            $http.post('/api/tower/save', payload)
                    .success(function (response) {
                        $log.debug(response);
                        $scope.data.tower = response;
                        alert("Tower id dengan [" + $scope.data.tower.id + "] telah disimpan!");
                        $scope.getTowers();
                    }).error(function (err) {
                alert("Tower id dengan [" + $scope.data.tower.id + "] gagal tersimpan!");
                $log.debug(err);
            });
        };


    }


})();
