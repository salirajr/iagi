
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

        $scope.list = {};
        $scope.list.investments = [];

        $scope.getAparkostList = function () {
            $http.get("/api/investment/ret")
                    .then(function (response) {
                        $scope.list.investments = response.data;
                        $log.debug($scope.list.investments);
                    });
        };
        $scope.getAparkostList();

    }


})();
