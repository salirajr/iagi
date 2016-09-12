
(function () {

    angular.getApplicationModule()
            .controller('StaffCtrl', StaffCtrl);

    /*
     * MainCtrl - controller
     */
    function StaffCtrl($scope, $log) {
        $log.debug('StaffCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';


        $scope.showDialog = function () {
            angular.element('#myModal6').modal('show');
        };


    }




})();
