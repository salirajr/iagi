
(function () {

    angular.getApplicationModule()
            .controller('LookupCtrl', LookupCtrl);

    /*
     * MainCtrl - controller
     */
    function LookupCtrl($scope, $rootScope, $log, $http) {

        $scope.getItems = function () {
            $http.get("/api/lookup/ret/findByGroupName?value=" + $scope.data.lookup.groupName)
                    .then(function (response) {
                        $scope.lookups = response.data;
                        angular.copy($scope.lookups[0], $scope.data.lookup);
                        $scope.temp.lookup = $scope.lookups[0];
                    });
        };

        function initiate() {
            $scope.lookup = {};
            $scope.data = {};
            $scope.temp = {};
            $scope.data.lookup = {};



            $http.get("/api/lookup/ret/listGroupName")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.groups = response.data;
                        $scope.data.lookup.groupName = $scope.lookup.groups[0];
                        $scope.getItems();
                    });
        }
        initiate();

        $scope.set = function () {
            angular.copy($scope.temp.lookup, $scope.data.lookup);
        };


        $scope.save = function () {
            var isConfirm = confirm("Anda akan menyimpan data referensi berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            var payload = {};
            angular.copy($scope.data.lookup, payload);
            $http.post('/api/lookup/save', payload)
                    .success(function (response) {
                        $log.debug(response);
                        $scope.data.lookup = response;
                        alert("Referensi id dengan [" + $scope.data.lookup.id + "] telah disimpan!");
                    }).error(function (err) {
                alert("Referensi id dengan [" + $scope.data.lookup.id + "] gagal tersimpan!");
                $log.debug(err);
            });
        };

        $scope.delete = function () {
            var isConfirm = confirm("Anda akan Menghapus data referensi berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            $http.post('/api/lookup/rem', $scope.data.lookup.id)
                    .success(function () {
                        alert("Referensi id dengan [" + $scope.data.lookup.id + "] telah dihapus!");
                    }).error(function (err) {
                alert("Referensi id dengan [" + $scope.data.lookup.id + "] gagal dihapus!");
                $log.debug(err);
            });
        };

        $scope.add = function () {
            alert("Referensi akan ditambahkan.");
            var temp = {};
            temp.groupName = $scope.data.lookup.groupName;
            angular.copy(temp, $scope.data.lookup);
        };


    }

})();
