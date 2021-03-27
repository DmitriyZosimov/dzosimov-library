package com.epam.brest.webapp;

import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.service.IReaderService;
import com.epam.brest.service.exception.ReaderCreationException;
import com.epam.brest.service.exception.ReaderNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ProfileControllerMockTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private IReaderService readerService;
    @Spy
    private final Model model = new ExtendedModelMap();
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpSession session;

    @Test
    public void test(){

    }

//    @Test
//    public void getProfileTest() throws ReaderNotFoundException {
//        ReaderDto readerDto = new ReaderDto();
//        Integer id = 1;
//        readerDto.setReaderId(id);
//
//        Mockito.when(readerService.getProfile(id)).thenReturn(readerDto);
//        Mockito.when(session.getAttribute(any(String.class))).thenReturn(1);
//        String result = profileController.getProfile(model, session);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("profile", result);
//
//        Assert.assertNotNull(model.getAttribute("readerDto"));
//        Assert.assertEquals(ReaderDto.class, model.getAttribute("readerDto").getClass());
//        ReaderDto resReaderDto = (ReaderDto) model.getAttribute("readerDto");
//        Assert.assertNotNull(resReaderDto);
//        Assert.assertEquals(id, resReaderDto.getReaderId());
//
//        Mockito.verify(readerService, Mockito.times(1)).getProfile(anyInt());
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(ReaderDto.class));
//    }
//
////    @Test(expected = ReaderNotFoundException.class)
////    public void getProfileWithReaderNotFoundExceptionTest() throws ReaderNotFoundException {
////        Mockito.when(readerService.getProfile(anyInt())).thenThrow(ReaderNotFoundException.class);
////        String result = profileController.getProfile(model, session);
////        Assert.assertNotNull(result);
////        Assert.assertEquals("error", result);
////        Mockito.verify(readerService, Mockito.times(1)).getProfile(anyInt());
////        Mockito.verify(model, Mockito.times(1)).
////                addAttribute(any(String.class), any(ReaderDto.class));
////    }
//
//    @Test()
//    public void createReaderGetTest() throws ReaderCreationException {
//        String result = profileController.createReader(model);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("reader", result);
//
//        Assert.assertNotNull(model.getAttribute("readerDto"));
//        Assert.assertEquals(ReaderDto.class, model.getAttribute("readerDto").getClass());
//        ReaderDto resReaderDto = (ReaderDto) model.getAttribute("readerDto");
//        Assert.assertNotNull(resReaderDto);
//
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(ReaderDto.class));
//
//    }
//
//    @Test
//    public void createReaderPostTest() throws ReaderNotFoundException, ReaderCreationException {
//        ReaderDto readerDto = new ReaderDto();
//        Integer id = 1;
//        readerDto.setReaderId(id);
//
//        /**
//         * all done correctly
//         */
//        Mockito.when(readerService.createReader(any(ReaderDto.class))).thenReturn(readerDto);
//        String result = profileController.createReader(new ReaderDto(), model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/result/card", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//        Assert.assertNotNull(model.getAttribute("card"));
//        Assert.assertEquals(id, (Integer) model.getAttribute("card"));
//
//        /**
//         * when bindingResult has errors
//         */
//        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
//        result = profileController.editProfile(new ReaderDto(), session, model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("error", result);
//
//        Mockito.verify(bindingResult, Mockito.times(2)).hasErrors();
//        //first is when all done correctly, second is when had badResult
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), anyBoolean());
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), anyInt());
//        Mockito.verify(readerService, Mockito.times(1)).
//                createReader(any(ReaderDto.class));
//    }
//
////    @Test(expected = ReaderCreationException.class)
////    public void createReaderWithException() throws ReaderCreationException {
////        Mockito.when(readerService.createReader(any(ReaderDto.class))).thenThrow(ReaderCreationException.class);
////        String result = profileController.createReader(new ReaderDto(), model, bindingResult);
////        Assert.assertNotNull(result);
////        Assert.assertEquals("error", result);
////    }
//
//    @Test()
//    public void editProfileGetTest() throws ReaderNotFoundException {
//        ReaderDto readerDto = new ReaderDto();
//        Integer id = 1;
//        readerDto.setReaderId(id);
//
//        Mockito.when(readerService.getProfileWithoutBooks(id)).thenReturn(readerDto);
//        Mockito.when(session.getAttribute(any(String.class))).thenReturn(1);
//        Mockito.when(model.getAttribute(any(String.class))).thenReturn(readerDto);
//        String result = profileController.editProfile(model, session);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("reader", result);
//
//        Assert.assertNotNull(model.getAttribute("readerDto"));
//        Assert.assertEquals(ReaderDto.class, model.getAttribute("readerDto").getClass());
//        ReaderDto resReaderDto = (ReaderDto) model.getAttribute("readerDto");
//        Assert.assertNotNull(resReaderDto);
//        Assert.assertEquals(id, resReaderDto.getReaderId());
//
//        Mockito.verify(readerService, Mockito.times(1)).getProfileWithoutBooks(anyInt());
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), any(ReaderDto.class));
//
//    }
//
//    @Test
//    public void editProfilePostTest() throws ReaderNotFoundException {
//        ReaderDto readerDto = new ReaderDto();
//        Integer id = 1;
//        readerDto.setReaderId(id);
//
//        /**
//         * all done correctly
//         */
//        Mockito.when(readerService.getProfile(id)).thenReturn(readerDto);
//        Mockito.when(session.getAttribute(any(String.class))).thenReturn(1);
//        Mockito.when(readerService.editProfile(any(ReaderDto.class))).thenReturn(true);
//        String result = profileController.editProfile(new ReaderDto(), session, model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("profile", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//        //getProfile
//        Assert.assertNotNull(model.getAttribute("readerDto"));
//        Assert.assertEquals(ReaderDto.class, model.getAttribute("readerDto").getClass());
//        ReaderDto resReaderDto = (ReaderDto) model.getAttribute("readerDto");
//        Assert.assertNotNull(resReaderDto);
//        Assert.assertEquals(id, resReaderDto.getReaderId());
//
//        /**
//         * when editProfile give badResult
//         */
//        Mockito.when(session.getAttribute(any(String.class))).thenReturn(1);
//        Mockito.when(readerService.getProfile(id)).thenReturn(readerDto);
//        Mockito.when(readerService.editProfile(any(ReaderDto.class))).thenReturn(false);
//        result = profileController.editProfile(new ReaderDto(), session, model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("profile", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertFalse((Boolean) model.getAttribute("result"));
//        //getProfile
//        Assert.assertNotNull(model.getAttribute("readerDto"));
//        Assert.assertEquals(ReaderDto.class, model.getAttribute("readerDto").getClass());
//        resReaderDto = (ReaderDto) model.getAttribute("readerDto");
//        Assert.assertNotNull(resReaderDto);
//        Assert.assertEquals(id, resReaderDto.getReaderId());
//
//        /**
//         * when bindingResult has errors
//         */
//        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
//        result = profileController.editProfile(new ReaderDto(), session, model, bindingResult);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("error", result);
//
//        Mockito.verify(bindingResult, Mockito.times(3)).hasErrors();
//        //first is when all done correctly, second is when had badResult
//        Mockito.verify(model, Mockito.times(2)).
//                addAttribute(any(String.class), anyBoolean());
//        Mockito.verify(model, Mockito.times(2)).
//                addAttribute(any(String.class), any(ReaderDto.class));
//        Mockito.verify(readerService, Mockito.times(2)).
//                editProfile(any(ReaderDto.class));
//        Mockito.verify(readerService, Mockito.times(2)).
//                getProfile(anyInt());
//    }
//
//    @Test
//    public void removeProfileTest(){
//        /**
//         * all done correctly
//         */
//        Mockito.when(session.getAttribute(any(String.class))).thenReturn(1);
//        Mockito.when(readerService.removeProfile(anyInt())).thenReturn(true);
//        String result = profileController.removeProfile( model, session);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/catalog", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//        /**
//         * when removeProfile give badResult
//         */
//        Mockito.when(readerService.removeProfile(anyInt())).thenReturn(false);
//        result = profileController.removeProfile(model, session);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/profile", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertFalse((Boolean) model.getAttribute("result"));
//
//        //first is when all done correctly, second is when had badResult
//        Mockito.verify(model, Mockito.times(2)).
//                addAttribute(any(String.class), anyBoolean());
//        Mockito.verify(readerService, Mockito.times(2)).
//                removeProfile(anyInt());
//    }
//
//    @Test
//    public void restoreProfileTest(){
//        ReaderDto readerDto = new ReaderDto();
//        Integer id = 1;
//        readerDto.setReaderId(id);
//
//        /**
//         * all done correctly
//         */
//        Mockito.when(readerService.restoreProfile(anyInt())).thenReturn(true);
//        String result = profileController.restoreProfile(1, model);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/result/card", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertTrue((Boolean) model.getAttribute("result"));
//        Assert.assertNotNull(model.getAttribute("card"));
//
//        /**
//         * when editProfile give badResult
//         */
//        Mockito.when(readerService.restoreProfile(anyInt())).thenReturn(false);
//        result = profileController.restoreProfile(1, model);
//        Assert.assertNotNull(result);
//        Assert.assertEquals("redirect:/catalog", result);
//        Assert.assertNotNull(model.getAttribute("result"));
//        Assert.assertFalse((Boolean) model.getAttribute("result"));
//
//        //first is when all done correctly, second is when had badResult
//        Mockito.verify(model, Mockito.times(2)).
//                addAttribute(any(String.class), anyBoolean());
//        Mockito.verify(model, Mockito.times(1)).
//                addAttribute(any(String.class), anyInt());
//    }

}
