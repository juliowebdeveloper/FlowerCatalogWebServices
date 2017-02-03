package com.hanselandpetal.flowercatalog;

import com.hanselandpetal.flowercatalog.model.Flower;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

/**
 * Created by MAGNA2 on 30/01/2017.
 */

public class MockitoTest {
    @Mock
    List<Flower> flowerDatabaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testQuery(){
    }


}
