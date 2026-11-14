package bots;

import main.Player;
import main.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BotRandom extends Player {
    private Random rand;
    public BotRandom(String name,String r,String al, Long seed) {
        super(name, r, al);
        rand = new Random(seed);
    }
    public void getRoleUsed(String info) {
        List<String> parts = Arrays.asList(info.split(" "));
        switch (parts.getFirst()) {
            case "evil:":
                for (int i = 1; i < parts.size() - 1; i++) {
                    for(Status st:getStatuses()){
                        if(st.getName().equals(parts.get(i))){
                            st.setSuspicion(-1);
                        }
                    }
                }
        }
    }

    public List<String> getNominations() {
        List<String> nominations = new ArrayList<>();
        Status randomStatus = getStatuses().get(rand.nextInt(getStatuses().size()));
        while (randomStatus.getSuspicion() < -0.99|| !randomStatus.isAlive()) {
            randomStatus = getStatuses().get(rand.nextInt(getStatuses().size()));
        }
        nominations.add(randomStatus.getName());
        return nominations;
    }

    public String getOpinionOnNominated(List<String> playerNames) {
        return playerNames.get(rand.nextInt(playerNames.size()));
    }

    public String getRoleUsed(){
        List<Status> statusesPriority = new ArrayList<Status>();
        List<Status> statusesNotPriority = new ArrayList<>();
        for(Status status: getStatuses()){
            if(!status.getName().equals(getName())&&status.isAlive()&&status.getSuspicion()>-0.99){
                statusesPriority.add(status);
                //System.out.println(status.getName()+" "+status.getSuspicion());
            } else if (!status.getName().equals(getName())&&status.isAlive()){
                statusesNotPriority.add(status);
            }
        }
        if(!statusesPriority.isEmpty()){
            int n = rand.nextInt(statusesPriority.size());
            return statusesPriority.get(n).getName();
        } else {
            int n = rand.nextInt(statusesNotPriority.size());
            return statusesNotPriority.get(n).getName();
        }
    }
}