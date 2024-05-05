package com.mymerit.mymerit.api.payload.request;

import com.mymerit.mymerit.domain.entity.SolutionFile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class SolutionRequest {
     private List<SolutionFile> files;
     private String mainName;


  public SolutionRequest(String mainName){
       this.mainName = mainName;
  }

  public void addToFiles(SolutionFile file){
       files.add(file);
  }

}