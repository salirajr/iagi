
(function () {

    angular.getApplicationModule()
            .controller('CovenantCtrl', CovenantCtrl);

    /*
     * MainCtrl - controller
     */
    function CovenantCtrl($scope, $log, $http) {
        $log.debug('CovenantCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

        $scope.data = {};
        $scope.data.selectedCStaff = {};
        $scope.dInput = {};

        $scope.getInvestor = function () {
            $http.get("sdata/investor.json")
                    .then(function (response) {
                        $scope.data.cInvestor = response.data;
                        $log.debug("getInvestor responsed sucess");
                    });

        };
        $scope.getInvestor();
        
        $scope.selectCStaff = function () {
            $log.debug("selectCStaff called with "+$scope.dInput.selectedCStaffId);

            for (var i = 0; i < $scope.data.covenantStaff.length; i++) {
                if ($scope.data.covenantStaff[i].staffId == $scope.dInput.selectedCStaffId) {
                    $scope.data.selectedCStaff = $scope.data.covenantStaff[i];
                    break;
                }
            }
        };
        
        $scope.getCovenantStaff = function () {
            $http.get("sdata/staff.json")
                    .then(function (response) {
                        $scope.data.covenantStaff = response.data;
                        $scope.dInput.selectedCStaffId = response.data[0].staffId;
                        $scope.data.selectedCStaff = response.data[0];
                        $log.debug("getInvestor responsed sucess with " + $scope.dInput.selectedCStaffId);
                    });

        };
        $scope.getCovenantStaff();
        
    }


})();
