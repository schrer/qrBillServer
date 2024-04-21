package at.schrer.qrbill.data.model;

import lombok.*;

@Getter
@Builder
@Setter
public class CompanyModel {
    private String shopName;
    private String companyName;
    private String certId;
    private String uid;
}
