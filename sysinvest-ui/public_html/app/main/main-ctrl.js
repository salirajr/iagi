
(function () {

    angular.getApplicationModule()
            .controller('MainCtrl', MainCtrl);

    /*
     * MainCtrl - controller
     */
    function MainCtrl($scope, $log, $localStorage) {
        $log.debug('MainCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Landing Page';
        $scope.descriptionText = 'To be dashboard ...';

    }


})();
