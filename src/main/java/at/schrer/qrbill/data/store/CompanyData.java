package at.schrer.qrbill.data.store;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyData {
    private String name;
    private String publicName;
    private String url;
    @Builder.Default
    private List<String> uids = List.of();
    @Builder.Default
    private List<String> certIds = List.of();
}
