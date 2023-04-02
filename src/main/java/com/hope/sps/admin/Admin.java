package com.hope.sps.admin;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Admin extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetailsImpl userDetails;
}
