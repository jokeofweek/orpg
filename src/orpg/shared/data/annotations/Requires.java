package orpg.shared.data.annotations;

import java.lang.annotation.*;

import com.artemis.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Requires {
	Class<? extends Component>[] dependencies();
}
