
(function () {

    angular.getApplicationModule()
            .controller('MinorCtrl', MinorCtrl);

    /*
     * MainCtrl - controller
     */
    function MinorCtrl($scope, $log) {
        $log.debug('MinorCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

    }


})();
