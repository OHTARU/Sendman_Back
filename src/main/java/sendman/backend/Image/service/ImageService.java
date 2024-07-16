package sendman.backend.Image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.account.domain.Account;
import sendman.backend.Image.domain.Image;
import sendman.backend.Image.repository.ImageRepository;
import sendman.backend.account.repository.AccountRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;
    private final AccountRepository accountRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Boolean saveImage(User resUser, MultipartFile file) throws IOException {
        //가져온 파일에서 이름 및 확장자 추출
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.indexOf("."));

        //이름 유일성 부여
        String uuidFileName = UUID.randomUUID()+ext;

        //파일 객체 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        PutObjectRequest savefile = new PutObjectRequest(bucket,"image/"+uuidFileName,file.getInputStream(),metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        //s3 버킷에 전달
        amazonS3Client.putObject(savefile);

        //s3 버킷에서 url 가져오기
        String url = amazonS3Client.getUrl(bucket,"image/"+uuidFileName).toString();
        // 만료기간 설정 ( 7일 )
        LocalDate exp = LocalDate.now().plusWeeks(1);

        //db 저장
        if (accountRepository.findByEmail(resUser.getUsername()).isPresent()){
            Account account = accountRepository.findByEmail(resUser.getUsername()).get();
            imageRepository.save(Image.builder()
                    .url(url)
                    .account(account)
                    .exp(exp)
                    .build());
        }

        return true;
    }

    //매일 0시 5분
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    public void clearImageData(){
        List<Image> images = imageRepository.findByExp(LocalDate.now());

        imageRepository.deleteAll(images);
    }
}
