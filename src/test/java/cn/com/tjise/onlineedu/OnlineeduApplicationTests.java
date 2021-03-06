package cn.com.tjise.onlineedu;

import cn.com.tjise.onlineedu.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineeduApplicationTests
{
    
    @Test
    void contextLoads()
    {
    }
    
    @Test
    void testToken()
    {
        String token = TokenUtil.buildToken("3422123");
        System.out.println(token);
        // 验证token过期后，再重新申请是否发生改变
        try
        {
            Thread.sleep(12_000L);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        boolean b = TokenUtil.validToken(token);
        System.out.println(b);
        String token1 = TokenUtil.buildToken("3422123");
        System.out.println(token1);
        System.out.println(token1.equals(token));
    }
}
