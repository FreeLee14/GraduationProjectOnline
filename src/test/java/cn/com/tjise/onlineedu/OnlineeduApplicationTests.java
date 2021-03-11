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
    }
}
