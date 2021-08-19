package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity (name = "invoices")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @ApiModelProperty(value = "Invoice id (generated by application)", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "Invoice number (assigned by user)", required = true, example = "2021/05/09/0000001")
    @Column(name = "invoice_number")
    private String number;

    @ApiModelProperty(value = "Date invoice was created", required = true)
    private LocalDate date;

    @ApiModelProperty(value = "Company who bought the product/service", required = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer")
    private Company buyer;

    @ApiModelProperty(value = "Company who sold product/service", required = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller")
    private Company seller;

    @ApiModelProperty(value = "List of products/service", required = true)
    @JoinTable(name = "invoice_invoice_entries",
        joinColumns = {@JoinColumn (name = "invoice_id")},
        inverseJoinColumns = @JoinColumn(name = "invoice_entry_id"))
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<InvoiceEntry> invoiceEntries;
}
