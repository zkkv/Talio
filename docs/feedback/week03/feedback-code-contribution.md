# Code Contributions and Code Reviews

### Focused Commits

>Grade: **Very Good**

Feedback: How good your commits were. Were they Focused/Clear?
#### Tops:
- The commits have a good message.
- They are quite focus, and they usually touch a couple of classes.
> Examples of good commits: 
> - [1](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-66/-/commit/38a472accaa126bf721be3b8d38fcb4ca837c14f)
> - [2](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-66/-/commit/15ffd2dc1be8de6ba415bcf00c4ad5b3d748de8f)
#### Tips:
- The commits should not fail the pipeline.
- Should have more commits pe branch.
- Try to keep commit structure consistent between team members.
### Isolation

>Grade: **Sufficient**

Feedback: This represents the Isolation of your branches and MRs. Is a branch covering one feature? Is an MR too comprehensive? Did you push code directly to main?
#### Tops:
- The branches and MRs cover one thing.
#### Tips:
- You should have issues for every feature you merge to main.
- [This](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-66/-/merge_requests/16#8fefc18e8f80ad23b9e5d44c8bccb67e31dfea0e) is not really a feature, just a class added. Your MRs should link to user stories or acceptance criteria in the form of issues or tasks.
- Your MRs should pass the pipeline before being considered for review!
- You can do better in branch naming.

### Review-ability
>Grade: **Good**

Feedback:
#### Tops:
- MRs have few commits, therefore they are easy to review.
#### Tips:
- The MRs should have a better description. You should improve this.
- The MRs should have a little more commits than just adding a class.


### Code Reviews
>Grade: **Insufficient**

Feedback:
#### Tops:
- A couple of comments on one MR.
#### Tips:
- Try to write targeted commits on specific Classes, Methods or LOC.
- More people should do reviews.
#### Keep in mind:
- The more targeted the comments are, the better. (i.e. if it is evident that they are directed at the lines of code, method or class in question)
- The more improvements I see from the comments, the better.

### Build Server
>Grade: **Good**

Feedback: The pipeline did not fail too often, and when it did, it was on feature branches. Those were also fixed. The build time is fast and there are frequent commits.
#### Tip:
- Do not merge failed pipelines, try to fix them right away.
- If you know a pipeline passes when merged, try to merge main into the branch so that it is up-to-date.
- Push more frequently to avoid conflicts when working together!