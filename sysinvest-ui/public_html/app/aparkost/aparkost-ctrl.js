
(function () {

    angular.getApplicationModule()
            .controller('AparkostCtrl', AparkostCtrl);

    /*
     * MainCtrl - controller
     */
    function AparkostCtrl($scope, $log) {
        $log.debug('AparkostCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

        
    }


})();
