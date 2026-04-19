module lab5.kmeans.plugin {

    requires serviceloader.example;

    provides ex.api.AnalysisService
        with algo.KMeansService, algo.KMedianService;
}