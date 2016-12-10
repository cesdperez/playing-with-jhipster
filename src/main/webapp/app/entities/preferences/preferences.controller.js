(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('PreferencesController', PreferencesController);

    PreferencesController.$inject = ['$scope', '$state', 'Preferences', 'PreferencesSearch'];

    function PreferencesController ($scope, $state, Preferences, PreferencesSearch) {
        var vm = this;

        vm.preferences = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Preferences.query(function(result) {
                vm.preferences = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PreferencesSearch.query({query: vm.searchQuery}, function(result) {
                vm.preferences = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
