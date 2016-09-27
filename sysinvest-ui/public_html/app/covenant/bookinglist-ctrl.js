
(function () {

    angular.getApplicationModule()
            .controller('BookingListCtrl', BookingListCtrl);

    /*
     * MainCtrl - controller
     */
    function BookingListCtrl($rootScope, $scope, $log, $http, $window) {
        $log.debug('AcquisitionCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

        $scope.list = {};

        $scope.getAcquisition = function () {
            $http.get("/api/acquisition/ret?key=")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.list.acquisition = response.data;
                    });

        };
        $scope.getAcquisition();
        
        $rootScope.formatDate = function(milis){
            return new Date(milis).toISOString().slice(0, 19).replace('T', ' ');
        };

        $scope.generateAkad = function (id) {
            var isConfirm = confirm("Apakah anda yakin untuk men-generate akad berdasarkan data rekord berikut?");
            if (!isConfirm)
                return;
            $http.post('/api/acquisition/generateakad?id=' + id, {}, {responseType: 'arraybuffer'})
                    .success(function (response) {
                        $log.debug("generateakad success!");
                        var file = new Blob([response], {type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'});
                        var fileURL = URL.createObjectURL(file);
                        $window.open(fileURL);
                    }).error(function (err) {
                $log.debug(err);
            });

        };

    }




})();
