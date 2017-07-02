export class K8dConfig {

    public static kubernetesMasterUrl = 'http://kube.ygcloud.com';
    public static kubernetesDashboardUrl = K8dConfig.kubernetesMasterUrl + ':30000';
    public static kubernetesDomain = 'ygcloud.com';

    public static getAppDomain(name: string): string {
        return name + '.' + K8dConfig.kubernetesDomain;
    }
}
