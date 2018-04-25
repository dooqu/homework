package homework;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Comparator;
import homework.*;

/*
TalkParser的功能是负责将指定路径的文本文件中的内容转换成Talk集合；
readLinesFromFile方法：负责读取文本文件内容，按行读取，并返回List<String>类型给调用者
readTalkListFromFile方法：调用readLinesFromFile，获取返回值List<String>，并读取返回值中的每一行，通过正则表达式，匹配每一行代表的Talk对象的的标题和Talk的时长(title和duration)
parseFromFile，为公开方法，内部调用readTalkListFromFile
*/
public class TalkParser
{
    protected List<String> readLinesFromFile(String filepath) throws Exception, IOException
    {
        File inputFile = new File(filepath);
        if(inputFile.exists() == false)
            throw new IOException("input file not found:" + filepath);

        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(inputFile));
            String line = null;
            while((line = reader.readLine()) != null)
            {
                lines.add(line);
            }
            return lines;
        }
        finally
        {
            if(reader != null)
            {
                reader.close();
            }
        }        
    }

    protected List<Talk> readTalkListFromFile(String filepath) throws Exception
    { 
        List<Talk> talks = new ArrayList<Talk>();
        //通过正则表达式进行匹配，兼容性更强；如匹配lighting，转换对应的duration值为5
        Pattern pattern = Pattern.compile("(.+)\\s+((\\d+)min|(lightning))");
        List<String> lines = this.readLinesFromFile(filepath);
        for(int i = 0, j = lines.size(); i < j; i++)
        {
            Matcher m = pattern.matcher(lines.get(i));
            if(m.find())
            {
                String title = m.group(1);
                int dur = (m.group(3) != null)? Integer.parseInt(m.group(3)) : 5;
                talks.add(new Talk(title, dur));
            }
        }
        talks.sort(Comparator.reverseOrder());
        return talks;
    }

    public List<Talk> parseFromFile(String filepath) throws Exception, IOException
    {
        return this.readTalkListFromFile(filepath);
    }
}
