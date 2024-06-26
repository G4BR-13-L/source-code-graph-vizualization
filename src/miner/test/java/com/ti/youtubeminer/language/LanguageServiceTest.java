package com.ti.youtubeminer.language;

import com.ti.youtubeminer.video.language.ILanguageRepository;
import com.ti.youtubeminer.video.language.Language;
import com.ti.youtubeminer.video.language.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
public class LanguageServiceTest {

    @Autowired
    private LanguageService languageService;

    @Mock
    private ILanguageRepository languageRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCodeExistingLanguage() {
        String code = "en";
        Language mockLanguage = Language.builder().code(code).build();

        Mockito.when(languageRepository.findByCode(code)).thenReturn(Optional.of(mockLanguage));

        Language result = languageService.findByCode(code);

        Assert.notNull(result, "Language should not be null");
        Assert.isTrue(result.getCode().equals(code), "Language code should match");
    }

    @Test
    public void testFindByCodeNonExistingLanguage() {
        String code = "fr";

        Mockito.when(languageRepository.findByCode(code)).thenReturn(Optional.empty());
        Mockito.when(languageRepository.save(Mockito.any(Language.class))).thenAnswer(invocation -> {
            Language newLanguage = invocation.getArgument(0);
            newLanguage.setId(1L);
            return newLanguage;
        });

        Language result = languageService.findByCode(code);

        Assert.notNull(result, "Language should not be null");
        Assert.isTrue(result.getCode().equals(code), "Language code should match");
    }

    @Test
    public void testFindByCodeWithNullCode() {
        Language result = languageService.findByCode(null);
        // You can add more assertions here based on your business logic
    }
}