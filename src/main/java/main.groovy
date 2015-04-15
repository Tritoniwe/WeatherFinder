import exceptions.RunException

/**
 * Script for getting current weather information and pushing it to Geckoboard
 */

//args = [];   //Show help

args = ["-name", "Dnepropetrovsk"]  //Ok

try {
    def worker = new MainWorker();
    def desiredCity = worker.defineCity(worker.checkInput(args.size()>0?args:["-help"]));
    worker.publishWidgets(desiredCity.getFirst(), desiredCity.getSecond());
    println("Operation Completed");
}
catch (RunException e) {
    if (e.message.isEmpty()) return;
    else {
        println(e.message);
        println("Operation failed");
        return;
    }
}
catch (Throwable t) {
    t.printStackTrace();
    println("Unexpected error: ${t.getMessage()}");
}
