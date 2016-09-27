
(function () {

    angular.getApplicationModule()
            .controller('StaffCtrl', StaffCtrl);

    /*
     * MainCtrl - controller
     */
    function StaffCtrl($scope, $rootScope, $log, $http, ErrorHandler) {


        function initiate() {
            $scope.lookup = {};
            $scope.data = {};
            $scope.temp = {};
            $scope.data.staff = {};
            $rootScope.data.staff = {};


            $http.get("/api/lookup/ret/findByGroupName?value=PROVINCE")
                    .then(function (response) {
                        $scope.lookup.provinces = response.data;
                    });

            $http.get("/api/rank/ret?key")
                    .then(function (response) {
                        $scope.lookup.ranks = response.data;
                    });

            $http.get("/api/securityrole/ret?key")
                    .then(function (response) {
                        $scope.lookup.roles = response.data;
                    });


        }
        initiate();

        $scope.saveStaff = function () {
            var isConfirm = confirm("Anda akan menyimpan data staff berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            var payload = {};
            angular.copy($scope.data.staff, payload);
            payload.birthDate = new Date(payload.birthDate);
            $http.post('/api/staff/save', payload)
                    .success(function (response) {
                        $log.debug(response);
                        $scope.data.staff = response;
                        $rootScope.data.staff.id = response.id;
                        alert("Staff id dengan [" + $scope.data.staff.id + "] telah disimpan!");
                    }).error(function (err) {
                alert("Staff id dengan [" + $scope.data.staff.id + "] gagal tersimpan!");
                $log.debug(err);
            });


        };
    }

})();
