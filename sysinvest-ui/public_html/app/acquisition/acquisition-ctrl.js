
(function () {

    angular.getApplicationModule()
            .controller('AcquisitionCtrl', AcquisitionCtrl);

    /*
     * MainCtrl - controller
     */
    function AcquisitionCtrl($scope, $log, $http, $window) {
        $log.debug('AcquisitionCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';


        $scope.generateAkad = function (id) {
            var isConfirm = confirm("Apakah anda yakin untuk men-generate akad berdasarkan data rekord berikut?");
            if (!isConfirm)
                return;
            $http.post('/api/acquisition/generateakad?id=' + id, {}, {responseType: 'arraybuffer'})
                    .success(function (response) {
                        $log.debug("generateakad success!");
                        var file = new Blob([response], {type: 'application/pdf'});
                        var fileURL = URL.createObjectURL(file);
                        $window.open(fileURL);
                    }).error(function (err) {
                $log.debug(err);
            });

        };

    }




})();
