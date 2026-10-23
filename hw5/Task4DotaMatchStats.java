package semestr1.hw5;

public class Task4DotaMatchStats {
    public static void main(String[] args) {
        String[] heroes = {"Pudge", "Invoker", "Juggernaut", "Crystal Maiden", "Earthshaker"};
        int[] kills   = {8,  12, 10, 2,  4};
        int[] deaths  = {6,  3,  2,  9,  5};
        int[] assists = {14, 9,  8,  18, 16};
        int[] netWorth = {14500, 21200, 24800, 9800, 12300};
        double[] kdaRatios = new double[heroes.length];
        int totalKills = 0;
        int totalNetWorth = 0;

        for (int i = 0; i < heroes.length; i++) {
            totalKills += kills[i];
            totalNetWorth += netWorth[i];

        }


        double maxKda = -1.0;
        int mvpIndex = -1;

        System.out.println("========================================================================");
        System.out.println("                       🏆 DOTA 2: POST-MATCH REPORT");
        System.out.println("========================================================================");
        System.out.printf("%-16s | %-10s | %-9s | %-9s | %s\n", "Герой", "K / D / A", "Net Worth", "KDA Ratio", "Kill Participation");
        System.out.println("------------------------------------------------------------------------");
        for (int i = 0; i < heroes.length; i++) {
            int effectiveDeaths = Math.max(1, deaths[i]);
            kdaRatios[i] = (double) (kills[i] + assists[i]) / effectiveDeaths;
            double killParticipation = ((double) (kills[i] + assists[i]) / totalKills) * 100.0;
            if (kdaRatios[i] > maxKda) {
                maxKda = kdaRatios[i];
                mvpIndex = i;
            }

String kdaStats = kills[i] + " / " + deaths[i] + " / " + assists[i];
            System.out.printf("%-16s | %-10s | %-9d | %-9.2f | %.2f%%\n",
                    heroes[i], kdaStats, netWorth[i], kdaRatios[i], killParticipation);
        }

        System.out.println("------------------------------------------------------------------------");
        System.out.println("Загальна кількість вбивств команди: " + totalKills);
        System.out.printf("Сумарний Net Worth команди:          %,d золота\n", totalNetWorth);
        System.out.printf("⭐ НАЙКРАЩИЙ ГРАВЕЦЬ МАТЧУ (MVP):    %s (KDA: %.2f)\n", heroes[mvpIndex], maxKda);
        System.out.println("========================================================================");






    }







}
