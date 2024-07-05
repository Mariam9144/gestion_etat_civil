package com.fst.back_etat_civil.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "citoyen")
@ToString
public class Citoyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;



    
    private String niciv;


   
    private String nom;

    
    private String prenom;


    
    private Date dateNaissance;

    private String telephone;
    //@JsonIgnore
    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "prof_id")
    private Profession profession;

    @NotNull
    @Column( name = "genre")
    private String genre;
   
    @Column(name = "portrait")
    private String portrait;;

    @Column( name = "civilite")
    private String civilite;


    
    private String prenomPere;

   //@JsonIgnore
    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "profP_id")
    private Profession professionPere;


    
    private String nomMere;


    @Column( name = "prenom_mere")
    private String prenomMere;
    //@JsonIgnore
    @ManyToOne//(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "profM_id")
    private Profession professionMere;

    //@JsonIgnore
    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "lieuNaissance_id")
    private Vqf lieuNaissance;
    
    //@JsonIgnore
    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "adresse_id")
    private Vqf adresse;
    
   
    private String rue;


   
    private String porte;
    
    
    private String autre;
    
    private String cle;

}
