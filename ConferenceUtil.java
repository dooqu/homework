package homework;
import java.util.List;

public class ConferenceUtil
{
    public static int getTotalDuration(List<Talk> talks)
    {
        int total = 0;
        for(int i = 0,j = talks.size(); i < j; i++)
            total += talks.get(i).getDuration();
        
        return total;
    }

    public static int getMaxCapacity(List<Session> sessions)
    {
        int max = 0;
        for(Session s : sessions)
        {
            max += s.getMaxCapacity();
        }

        return max;
    }

    public static boolean tryToExangeTalkFromOther(Session s1, Session s2)
    {
        if(s1.getTalks().size() < 3)
            return false;

        int minIndex = 0;
        int minValue = s1.getTalks().get(0).getDuration();

        for(int i = 1; i < s1.getTalks().size(); i++)
        {
            if(s1.getTalks().get(i).getDuration() < minValue)
            {
                minIndex = i;
                minValue = s1.getTalks().get(i).getDuration();
            }
        }

        if(minValue == 60)
            return false;

        for(int i = 0; i < s2.getTalks().size(); i++)
        {
            Talk currTalk = s2.getTalks().get(i);
            if(currTalk.getDuration() <= minValue)
                continue;
            
            if(s2.getMinCapacity() == 180)
            {
                int dis = currTalk.getDuration() - minValue;

                if((s2.getCapacityLeft() - dis) > 60)
                {
                    return false;
                }
            } 
            
            //交换
            s2.talks.set(i, s1.getTalks().get(minIndex));
            s1.getTalks().set(minIndex, currTalk);

            return true;
        }

        return false;
    }
}