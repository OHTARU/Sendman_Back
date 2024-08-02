package sendman.backend.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsBucket {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public String saveFile(MultipartFile file, String path) throws IOException {
        //가져온 파일에서 이름 및 확장자 추출
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.indexOf("."));

        //이름 유일성 부여
        String uuidFileName = UUID.randomUUID()+ext;

        //파일 객체 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        PutObjectRequest savefile = new PutObjectRequest(bucket,path+"/"+uuidFileName,file.getInputStream(),metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        //s3 버킷에 전달
        amazonS3Client.putObject(savefile);

        //s3 버킷에서 url 가져오기
        return amazonS3Client.getUrl(bucket,path+"/"+uuidFileName).toString();
    }
}
