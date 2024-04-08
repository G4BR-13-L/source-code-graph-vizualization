package com.ti.youtubeminer.report;

import com.ti.youtubeminer.apikey.ApiKeyService;
import com.ti.youtubeminer.missioncotrol.MissionControl;
import com.ti.youtubeminer.missioncotrol.MissionControlService;
import com.ti.youtubeminer.searchterm.SearchTermService;
import com.ti.youtubeminer.tag.TagService;
import com.ti.youtubeminer.video.VideoService;
import com.ti.youtubeminer.video.topic.TopicService;
import com.ti.youtubeminer.youtubelistresponse.YouTubeSearchListPageResponseService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    @Value("${application.mining.start.report.file.path}")
    private String reportStartFilePath;

    @Value("${application.mining.end.report.file.path}")
    private String reportEndFilePath;

    @Value("${application.mining.programming_language_mining_flag}")
    private String PROGRAMMING_LANGUAGE_MINING_FLAG;

    @Value("${application.mining.search_terms}")
    private String SEARCH_TERMS;

    @Value("${application.mining.video_amount}")
    private String VIDEO_AMOUNT;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @Autowired
    private SearchTermService searchTermService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private MissionControlService missionControlService;

    @Autowired
    private TagService tagService;

    @Autowired
    private YouTubeSearchListPageResponseService youTubeSearchListPageResponseService;


    @Autowired
    private TopicService topicService;


    @PostConstruct
    public void writeStartReportToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportStartFilePath))) {
            writer.println("====================== RELATÓRIO DE CONFIGURAÇÃO =====================\n");
            writer.println("MINERAÇÃO DE VÍDEOS --------------------------------------------------");
            writer.println("[ " + searchTermService.count() + " ] termos de pesquisa de vídeos serão utilizados");
            writer.println("[ " + PROGRAMMING_LANGUAGE_MINING_FLAG + " ] Utilizado como contexto de mineração");
            writer.println("[ " + VIDEO_AMOUNT + " ] vídeos serão minerados");
            writer.println("[ " + Integer.parseInt(VIDEO_AMOUNT) / 50 + " ] Requisições de pesquisa por vídeos serão feitas ao YouTube");
            writer.println("[ " + Integer.parseInt(VIDEO_AMOUNT) / 50 + " ] Requisições de coleta de mais detalhes de vídeos serão feitas ao YouTube");
            writer.println("----------------------------------------------------------------------\n");

            writer.println("================== DIRETIVA DA MINERAÇÃO ( PADRÃO ) ==================");
            writer.println("                 Continuar a Mineração de onde parou!");
            writer.println("----------------------------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void writeEndReportToFile() {

        MissionControl missionControl = missionControlService.getMissionControl();

        printEndReport(missionControl.getStartTime());

        Instant startTime = Instant.ofEpochMilli(missionControl.getStartTime());
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportEndFilePath))) {
            writer.println("\n\n\n\n====================== RELATÓRIO DE MINERAÇÃO =====================\n");
            writer.println("MINERAÇÃO DE VÍDEOS --------------------------------------------------");
            writer.println("[ " + (apiKeyService.count()) + " ] chaves de API foram utilizadas");
            writer.println("-----");
            writer.println("[ " + (searchTermService.countAllSearched()) + " ] termos de pesquisa de vídeos foram utilizados");
            writer.println("[ " + (youTubeSearchListPageResponseService.count()) + " ] páginas de pesquisa foram produzidas");
            writer.println("-----");
            writer.println("[ " + (videoService.count()) + " ] vídeos foram minerados no total");
            writer.println("-----");
            writer.println("[ " + (videoService.countUniqueVideos()) + " ] são vídeos unicos");
            writer.println("[ " + (videoService.countMinedDetatilsVideos()) + " ] são videos completamente minerados (Tempo do video)");
            writer.println("[ " + (videoService.countInvalidVideos()) + " ] são videos invalidados pelo miner ( contando com as duplicatas ) ");
            writer.println("[ " + (videoService.countRemovedVideos()) + " ] são videos removidos pelo dono do canal ( contando com as duplicatas )");
            writer.println("-----");
            writer.println("-----");
            writer.println("[ " + (tagService.count()) + " ] Tags foram salvas");
            writer.println("[ " + (topicService.count()) + " ] Tags foram descobertos");
            writer.println("\nTEMPO DE MINERAÇÃO --------------------------------------------------");
            writer.println("Início da mineração: " + dateTimeFormatter.format(startTime));
            writer.println("Fim da mineração: " + dateTimeFormatter.format(endTime));
            writer.println("Tempo de mineração: " + formatDuration(duration));
            writer.println("\n====================================================================");
            writer.println("----------------------------------------------------------------------\n\n\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        missionControlService.clean();
    }

    public void printEndReport(long missionControlStartTime) {
        Instant startTime = Instant.ofEpochMilli(missionControlStartTime);
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);

        long apiKeyCount = apiKeyService.count();
        long searchTermCount = searchTermService.countAllSearched();
        long youTubeSearchListPageResponseCount = youTubeSearchListPageResponseService.count();
        long videoCount = videoService.count();
        long videoUniqueCount = videoService.countUniqueVideos();
        long tagCount = tagService.count();
        long topicCount = topicService.count();
        long minedDetatilsVideosCount = videoService.countMinedDetatilsVideos();
        long invalidVideosCount = videoService.countInvalidVideos();
        long removedVideosCount = videoService.countRemovedVideos();

        System.out.println("\n\n\n\n====================== RELATÓRIO DE MINERAÇÃO =====================\n");
        System.out.println("MINERAÇÃO DE VÍDEOS --------------------------------------------------");
        System.out.println("[ " + apiKeyCount + " ] chaves de API foram utilizadas");
        System.out.println("-----");
        System.out.println("[ " + searchTermCount + " ] termos de pesquisa de vídeos foram utilizados");
        System.out.println("[ " + youTubeSearchListPageResponseCount + " ] páginas de pesquisa foram produzidas");
        System.out.println("-----");
        System.out.println("[ " + videoCount + " ] vídeos foram minerados no total");
        System.out.println("-----");
        System.out.println("[ " + videoUniqueCount + " ] são vídeos unicos");
        System.out.println("[ " + minedDetatilsVideosCount + " ] são videos completamente minerados (Tempo do video)");
        System.out.println("[ " + invalidVideosCount + " ] são videos invalidados pelo miner ( contando com as duplicatas ) ");
        System.out.println("[ " + removedVideosCount + " ] são videos removidos pelo dono do canal ( contando com as duplicatas )");
        System.out.println("-----");
        System.out.println("[ " + tagCount + " ] Tags foram salvas");
        System.out.println("[ " + topicCount + " ] Topicos foram descobertos");
        System.out.println("\nTEMPO DE MINERAÇÃO --------------------------------------------------");
        System.out.println("Início da mineração: " + dateTimeFormatter.format(startTime));
        System.out.println("Fim da mineração: " + dateTimeFormatter.format(endTime));
        System.out.println("Tempo de mineração: " + formatDuration(duration));
        System.out.println("\n====================================================================");
        System.out.println("----------------------------------------------------------------------\n\n\n\n");
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long milliseconds = duration.toMillisPart();
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }
}

